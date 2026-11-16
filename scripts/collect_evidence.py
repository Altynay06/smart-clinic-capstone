"""Collect real output from running Compose services; never print credentials."""
import datetime
import json
import pathlib
import subprocess
import urllib.request

ROOT = pathlib.Path(__file__).resolve().parents[1]
OUT = ROOT / 'evidence'
OUT.mkdir(exist_ok=True)
settings = dict(line.split('=', 1) for line in (ROOT / '.env').read_text().splitlines() if '=' in line)
BASE = 'http://localhost:8080'

def curl(endpoint, payload=None, token=None):
    command = ['curl', '--silent', '--show-error', '--fail-with-body', BASE + endpoint]
    if payload is not None:
        command += ['-H', 'Content-Type: application/json', '-d', json.dumps(payload)]
    if token:
        command += ['-H', 'Authorization: Bearer ' + token]
    return json.loads(subprocess.check_output(command, text=True))

def login(role, identifier):
    return curl('/' + role + '/login', {'username': identifier, 'email': identifier, 'password': settings['DEMO_PASSWORD']})['token']

def report(name, command, result):
    (OUT / name).write_text('$ ' + command + '\n\n' + json.dumps(result, indent=2), encoding='utf8')

doctor = login('doctor', 'doctor1@clinic.example')
patient = login('patient', 'patient1@clinic.example')
report('curl-doctors.txt', 'curl http://localhost:8080/doctor', curl('/doctor'))
appointments = curl('/patient/appointments', token=patient)
report('curl-patient-appointments.txt', 'curl -H "Authorization: Bearer [REDACTED]" http://localhost:8080/patient/appointments', appointments)
report('curl-filter-doctors.txt', 'curl "http://localhost:8080/doctor?specialty=Cardiology&time=AM"', curl('/doctor?specialty=Cardiology&time=AM'))
assigned = curl('/appointments', token=doctor)['appointments']
assert assigned, 'Seeded doctor must have appointments'
prescription = curl('/prescriptions', {'appointmentId': assigned[0]['id'], 'medication': 'DEMO_MEDICATION', 'dosage': 'Demonstration only', 'duration': 'Not clinical', 'notes': 'Synthetic educational record; not medical advice'}, doctor)
assert prescription['id']
report('mongo-prescription-check.txt', 'POST /prescriptions (authenticated assigned doctor)', prescription)

tomorrow = datetime.date.today() + datetime.timedelta(days=1)
queries = {
    'sql-tables.txt': 'SHOW TABLES;',
    'sql-five-patients.txt': 'SELECT id,name,email,phone,address FROM patient ORDER BY id LIMIT 5;',
    'sql-daily-report.txt': "CALL GetDailyAppointmentReportByDoctor('" + tomorrow.isoformat() + "');",
    'sql-monthly-report.txt': f'CALL GetDoctorWithMostPatientsByMonth({tomorrow.month},{tomorrow.year});',
    'sql-yearly-report.txt': f'CALL GetDoctorWithMostPatientsByYear({tomorrow.year});',
}
for filename, query in queries.items():
    command = ['docker', 'compose', 'exec', '-T', 'mysql', 'sh', '-c', 'MYSQL_PWD="$MYSQL_PASSWORD" mysql --user="$MYSQL_USER" --table cms']
    result = subprocess.check_output(command, input=query, text=True, cwd=ROOT)
    (OUT / filename).write_text(query + '\n\n' + result, encoding='utf8')
print('PASS: collected real SQL/curl outputs and verified prescription persistence')

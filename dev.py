#!/usr/bin/env python3
"""Start this repository's local demo; Ctrl-C stops only its child processes."""
from pathlib import Path
import subprocess, os, sys, time, urllib.request, signal, socket
root=Path(__file__).resolve().parent
processes=[]
def load_env():
    path=root/'.env'
    if path.exists():
        for line in path.read_text().splitlines():
            if line.strip() and not line.lstrip().startswith('#') and '=' in line:
                k,v=line.split('=',1);os.environ.setdefault(k.strip(),v.strip().strip('"').strip("'"))
def wait(port,process):
    for _ in range(120):
        if process.poll() is not None: raise RuntimeError('Service exited; inspect .run/*.log')
        try:
            with urllib.request.urlopen(f'http://localhost:{port}/actuator/health',timeout=2) as r:
                if r.status==200:return
        except Exception:pass
        time.sleep(1)
    raise RuntimeError(f'Health check timed out on {port}')
def start(module,port):
    try:
        with socket.create_connection(("localhost",port),0.2):
            raise RuntimeError(f"Port {port} is already occupied; stop the other activity first")
    except OSError:pass
    folder=root/module if module else root
    jars=[p for p in (folder/'target').glob('*.jar') if not p.name.endswith('.original')]
    if len(jars)!=1:raise RuntimeError('Build first: ./mvnw verify')
    log=(root/'.run'/(module or 'bank')).with_suffix('.log').open('w')
    java=str(Path(os.environ['JAVA_HOME'])/'bin/java') if os.environ.get('JAVA_HOME') else 'java'
    p=subprocess.Popen([java,'-Xmx384m','-jar',str(jars[0])],cwd=root,stdout=log,stderr=subprocess.STDOUT)
    processes.append(p);print(f'Starting {module or "bank-service"} on {port}',flush=True);wait(port,p)
try:
    load_env();(root/'.run').mkdir(exist_ok=True)
    start('config-service',8888)
    start('discovery-service',8761)
    start('customer-service',8081)
    start('ebank-service',8082)
    start('gateway-service',8880)
    if os.environ.get('AI_PROVIDER','ollama')=='ollama' or os.environ.get('OPENAI_API_KEY'):start('chatbot-service',8084)
    else:print('Backend ready. Set your own OPENAI_API_KEY in .env to enable AI chat.')
    if (root/'frontend').exists():
        if not (root/'frontend/node_modules').exists():raise RuntimeError('Install frontend: cd frontend && npm ci')
        p=subprocess.Popen(['npm','start'],cwd=root/'frontend');processes.append(p)
        print('Frontend: http://localhost:4200',flush=True)
    print('Ready. Ctrl-C stops this demo. Logs: .run/',flush=True)
    while True:
        if any(p.poll() is not None for p in processes):raise RuntimeError('A process stopped; inspect .run/')
        time.sleep(2)
except KeyboardInterrupt:pass
except Exception as e:print(str(e),file=sys.stderr);sys.exitcode=1
finally:
    for p in reversed(processes):p.terminate()
    for p in reversed(processes):
        try:p.wait(timeout=10)
        except subprocess.TimeoutExpired:p.kill()
    sys.exit(getattr(sys,'exitcode',0))

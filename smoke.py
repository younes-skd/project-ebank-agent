#!/usr/bin/env python3
import json, urllib.request, sys
def req(port,path,body=None,method=None):
    request=urllib.request.Request(f'http://localhost:{port}{path}',data=json.dumps(body).encode() if body is not None else None,method=method,headers={'Content-Type':'application/json','Origin':'http://localhost:4200'})
    with urllib.request.urlopen(request,timeout=180) as response:
        if port==8880:
            assert response.headers.get_all('Access-Control-Allow-Origin')==['http://localhost:4200'], 'Gateway must return one CORS origin header'
        data=response.read();return json.loads(data) if data else None

tools=req(8084,'/mcp-tools');assert any('listAccounts' in t for t in tools),tools
assert any('listCustomers' in t for t in tools),tools
accounts=req(8880,'/api/accounts');assert accounts
assert req(8082,'/accounts/'+accounts[0]['id'])['customerStatus']['available'] is True
assert req(8888,'/ebank-service/default')['propertySources']
if '--chat' in sys.argv:
    answer=req(8084,'/chat',{'conversationId':'smoke-real','message':'Utilise les outils MCP pour lister les comptes et leurs soldes exacts.'})
    assert answer['answer'];print(answer['answer'])
print('PASS: MCP discovery, Gateway, Feign and Config Server')

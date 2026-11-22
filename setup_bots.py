#!/usr/bin/env python3
"""Read Telegram chat IDs using the token in .env, without printing the token."""
from pathlib import Path
import json,sys,urllib.request,urllib.error
env={}
for line in (Path(__file__).resolve().parent/'.env').read_text().splitlines():
    if line.strip() and not line.lstrip().startswith('#') and '=' in line:
        k,v=line.split('=',1);env[k.strip()]=v.strip().strip('"').strip("'")
if sys.argv[1:]!=['telegram']:raise SystemExit('Usage: python3 setup_bots.py telegram')
token=env.get('TELEGRAM_BOT_TOKEN','')
if not token:raise SystemExit('Set TELEGRAM_BOT_TOKEN in .env first')
request=urllib.request.Request('https://api.telegram.org/bot'+token+'/getUpdates',data=b'{"timeout":0}',headers={'Content-Type':'application/json'})
try:
    with urllib.request.urlopen(request,timeout=10) as response:data=json.load(response)
except Exception:raise SystemExit('Telegram request failed. Verify the token and stop any other getUpdates client.') from None
ids={str(item['message']['chat']['id']) for item in data.get('result',[]) if 'message' in item}
if not ids:print('No chats found. Send /start to your bot and rerun.')
for chat_id in sorted(ids):print('Chat ID:',chat_id)

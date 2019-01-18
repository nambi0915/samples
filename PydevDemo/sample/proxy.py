import requests
import uuid

proxies = {
    'http': '127.0.0.1',
}
print(uuid.uuid4())
# Create the session and set the proxies.
s = requests.Session()
s.proxies = proxies

# Make the HTTP request through the session.
r = s.get('http://srvgdyplmmap01.nov.com')
print(r)

# Check if the proxy was indeed used (the text should contain the proxy IP).
print(r.reason)

Set up:
```bash
$ echo "IP=$(hostname -I | cut -f1 -d' ')" > .env
$ docker-compose up -d
```

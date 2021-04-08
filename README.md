## Webhook parrot

Starts simple http server and echoes requests incoming on localhost:1337/webhook to telegram bot.

### Build

```shell
mvn install
```

### Start

* Build project
* Unzip target/webhook_parrot.zip
* Set your telegram bot key to tg_key.txt
* call install.cmd
* Feel free to remove telegram bot key file

### Runtime

Provides following telegram commands:

* /start - add the current chat to bot mailing list;
* /stop - remove the current chat from bot mailing list.

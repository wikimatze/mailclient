During the summer term 2007 we had the task to create a Thunderbird like **email client**
in Java. This here is the collection of my code.


## Installation for NetBeans

- get the [source from github](https://github.com/matthias-guenther/mailclient )
- extract the *javamail-1.4.zip* => this package contains a Java library for handling emails
- create a new project under NetBeans and copy all files of the *Mail Client* directory in the *src*
  directory of you NetBeans project
- then you have to insert the *mail.jar* file in your project in the following way:
  - click right on Libraries in the near of the view of your NetBeans project and chose *Add JAR*
  - navigate to *javamail-1.4* folder and select the *mail.jar*
  - when you now select the files *GUIMailsend.java* and *GUIMailsget.java* should not show any errors
- now you have to adopt the paths in the file *GUIMain.java* `(static File f, String dir, FileReader
  file)` and *GUITree* `(File driveC, File source, File ziel)` to your NetBeans project folder
  through absolute paths => I know it's cumbersome but due to this date I couldn't do any other
- set the *GuiMain.java* as the main file in your project and start the program
- the standard account is *MG kontoinfos.kondat* which contains the necessary data to access my
  spam account (you can guess how to change the data)
- the directory *Inbox MG* saves all read mails of the user MG, further accounts have to be created
  each in an own directory
- *MailApi.java* is not necessary for the mailclient, this will be used to check the
  correctness of the POP3 settings via the terminal
- main cause of error messages:
  - wrong paths to your project
  - POP3 and SMTP
  - *mail.jar* is not included correct in the project, but this will be shown in NetBeans


## Images

Some pics so you have a clue what does it look like:

- Basic surface of the program
  - ![basic surface of the program](https://github.com/matthias-guenther/mailclient/raw/master/mail_client_1.png)
- Dialog where you can add new accounts
  - ![dialog to add new accounts](https://github.com/matthias-guenther/mailclient/raw/master/mail_client_2.png)
- Receive email of an already existing account
  - ![receive email](https://github.com/matthias-guenther/mailclient/raw/master/mail_client_3.png)
- Window to write a new email
  - ![write email](https://github.com/matthias-guenther/mailclient/raw/master/mail_client_4.png)
- Drag-and-Drop window to manage emails
  - ![drag-and-drop emails](https://github.com/matthias-guenther/mailclient/raw/master/mail_client_5.png)


## Contact

Feature request, bugs, questions, etc. can be send to <matthias.guenther@wikimatze.de>.


## License

This software is licensed under the [MIT license][mit].

© Matthias Günther <matthias@wikimatze.de>.

[mit]: http://en.wikipedia.org/wiki/MIT_License

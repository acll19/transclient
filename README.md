# transclient
A simple java-based translator which makes use of some translation services exposed on the internet as REST API's.
Currently it supports [MyMemory](http://mymemory.translated.net/doc/spec.php) and [Yandex](https://tech.yandex.com/translate/) which are both free.

*transclient* aims to help people translate large amount of texts (within the boundary of the above services) in a file or directory.
## Requirements
*transclient* is developed using java8 so java8 must be installed on your computer. you can download it from [here](https://java.com/)

## Compilation

*transclient* is compiled with maven. Download maven from [here](https://maven.apache.org/), put it in your environment variables, open a command line, move to *transclient* root directory and type `mvn package`. This will generate **transclient-1.0.jar** under the *target* directory. Enter that directory.
 
## Usage
### MyMemory

#### *First Time*
If it is the first time you use this service you need to get an apiKey. In a command line tool type `java -jar transclient.jar -t mymemory -u YourUserName -p YourPassword -lp en|es` this will get an apiKey for you. Then enter a valid file or directory path where the text to be translated is. From now on you have to execute this command line every time you want to use *transclient*.

#### *Already have an apiKey*
If you have used this service before and you already have an apiKey then you can provide that apiKey instead your username and password as follows: `java -jar transclient.jar -t mymemory --apiKey YourApiKey -lp en|es`

**Note that you can change your language settings to any language you wish (-lp en|es)**

### Yandex
You need to go to [Yandex](https://tech.yandex.com/translate/) web site to generate an apiKey. When you get your apiKey go to the command line tool and type the following: `java -jar transclient.jar -t yandex --apiKey YourApiKey -l en-es`. Then enter a valid file or directory path where the text to be translated is

**Note that you can change your language settings to any language you wish (-l en-es)**

## Note
*transclient will check both MyMemory and Yandex usage limits so you be careful with the amount of text you intend to process*


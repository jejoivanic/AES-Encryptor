# AES-Encryptor
Encryptador AES para la materia Seguridad Informatica.

## Como usar

El correcto modo de uso es (estando en la misma carpeta que el archivo .jar)

```
java -jar <archivo-jar>.jar <encrypt/decrypt> <archivo> <password>
```
IMPORTANTE : El archivo a encriptar/desencriptar tiene que estar en el mismo directorio que el archivo '.jar'.

## Error frecuente

Al utilizar el encriptador, aparece un error extraño como
```
java.security.InvalidKeyException: Illegal key size or default parameters
```

¿Qué debo hacer?

Este error se debe a las políticas de seguridad de ciertos países, las cuales establecen un límite al momento de generar una llave. Para solucionar esto se debe descargar, según corresponda
[Para Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html)
[Para Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
y pegar ls archivos en el sub-directorio dentro de la carpeta de instalación de Java: ```${java.home}/jre/lib/security/```
Esto sustituirá las políticas de seguridad por unas sin límites para la generación de llaves.
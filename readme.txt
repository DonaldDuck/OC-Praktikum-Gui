
Prerequisites
-------------

To run iControl you need at least JRE 6.0

Install instructions
--------------------

iControl can be used as a plugin inside the iShell tool

Also, since iControl uses the SWT framework, you have to
decide at compile-time which platform you want to use.

Building iControl is very easy. You don't need any external
libraries besides a working Java SDK and "ant".
The resulting jar files contain all needed libraries (via the
"one-jar" boot-mechanism) and are directly excecutable.



Building iShell with iControl included as a plugin
---------------------------------------------

Just run 

    $ ant ishell-linux

or

    $ ant ishell-win32


again depending on the platform you want to use 

These commands will build the file ishell-linux-iControl.jar
or ishell-win32-iControl.jar. This jar files includes a version of iShell,
the compiled iControl source and all libraries both applications depend on.

Similary, it can be run directly via

    $ java -jar ishell-linux-iControl.jar
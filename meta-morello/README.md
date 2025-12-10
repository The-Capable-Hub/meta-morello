meta-morello
============

Booting the hardware
--------------------

For information on how to boot the hardware and how the hardware is booting see [1] and [2].

Machines
--------

The machines have been split into:  
- morello-soc for the actual hardware
- morello-fvp for running a `FVP` image using `runfvp` script @ `meta-arm`

Building images
---------------

```
$ kas build ./kas/morello-soc.yml  
```
or  
```
$ kas build ./kas/morello-fvp.yml  
```
FVP
---

To run the FVP model:  
```
$ cd poky  
$ . oe-init-build-env ../build  
$ ./../meta-arm/scripts/runfvp --console tmp-fvp/deploy/images/morello-fvp/usb-image-morello-fvp.fvpconf  
```
or  
```
$ cd poky  
$ . oe-init-build-env ../build  
$ ./../meta-arm/scripts/runfvp tmp-fvp/deploy/images/morello-fvp/usb-image-morello-fvp.fvpconf  
```
Then inspect the FVP console output for information on your uart_ap port:  
```
$ terminal_uart_ap: Listening for serial connection on port 5003  
```
With that knowledge you can now run:  
```
$ telnet localhost 5003  
```
For further instructions on how to run the image with `FVP` go here [3]  

Images
------

The outputs can be found under build/temp/deploy/images:  
- board-firmware-sd-image.img goes on the SD card  via DD
- usb-image-morello-fvp/soc.wic goes on the USB via DD

Linux and musl-libc
-------------------

The linux kernel and musl-libc are locked in sync so that the release tags from upstream always match.


Known limitations
-----------------

- the current state of this layer is meant to be just a starting point and foundation for further development, the main aim was to have working `Linux` images ASAP for the community, do not expect elegant `Yocto` solutions yet


Contributing
------------

We accept patches through the mailing list or via PR to this repo.  

https://op-lists.linaro.org/mailman3/lists/linux-morello-distros.op-lists.linaro.org/


References
----------

[1] https://developer.arm.com/documentation/den0132/0100/Setting-up-the-Morello-Hardware-Development-Platform  
[2] https://developer.arm.com/documentation/102278/0001/?lang=en  
[3] https://github.com/jonmason/meta-arm/blob/master/documentation/runfvp.md  


maintainer
----------
* Pawel Zalewski <pzalewski@thegoodpenguin.co.uk>
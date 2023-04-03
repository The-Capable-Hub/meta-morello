meta-morello
============

Meta-morello provides the layers required to build the firmware that lives on the SD card and a `Morello` enabled
`Linux` kernel for the Morello System Development Platform.  

- Use the provided `kas` scripts to get all of the dependencies.
- Read on how to get and use `kas` here [1]

* meta-morello

This layer contains most of the code recipes: firmware, kernel, FVP etc.

* meta-morello-toolchain

This layer contains recipes for compilers (Morello LLVM and GCC) but also the C libraries.


Contributing
------------

We accept patches through the mailing list only.  

https://op-lists.linaro.org/mailman3/lists/linux-morello-distros.op-lists.linaro.org/

Check if the work is not already scheduled in the issues section [2].  

Follow the coding style found in other layers, the aim here is to keep them consistent where possible  
and very easy to read. Follow the order found in the "headers" of each recipe and in general.  

`.bb` recipes that come from Morello gitlab and are Morello forks of upstream inherit the name Morello in the recipe: package-name-morello  
`.bbappends` do not need to do this even if they come from Morello gitlab as they would only change `SRC_URI`, which is a bad practice but 
it is acceptable for now.

Whether to append or start a new recipe at this stage is up to the designer, whatever is the easiest.  

You should familiarize yourself with the following documents [3][4][5][6].  

References
----------

[1] https://kas.readthedocs.io/en/latest/  
[2] https://git.morello-project.org/morello/meta-morello/-/issues  
[3] https://github.com/ARM-software/abi-aa/blob/main/aaelf64-morello/aaelf64-morello.rst  
[4] https://git.morello-project.org/morello/kernel/linux/-/wikis/Transitional-Morello-pure-capability-kernel-user-Linux-ABI-specification  
[5] https://www.openembedded.org/wiki/Commit_Patch_Message_Guidelines  
[6] https://people.kernel.org/tglx/notes-about-netiquette  

maintainer
----------
* Pawel Zalewski <pzalewski@thegoodpenguin.co.uk>
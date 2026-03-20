require linux-morello.inc

LIC_FILES_CHKSUM  = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRCREV_machine = "5c1e26bca388372ca365e3e01ad32a930c4c92fa"
SRCREV_meta ?= "17375dce1754d0783fb3fb9e684691951f9ff357"

LINUX_VERSION           = "6.7"
LINUX_VERSION_EXTENSION = "-yocto-purecap"

COMPATIBLE_MACHINE = "morello"

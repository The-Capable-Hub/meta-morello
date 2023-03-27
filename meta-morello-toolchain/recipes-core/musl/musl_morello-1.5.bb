require musl-morello-${MORELLO_ARCH}.inc musl-morello-${MORELLO_ARCH}-so.inc musl-morello-target.inc

MACHINE_INC ?= ""
MACHINE_INC:morello-soc = "override-glibc.inc"
MACHINE_INC:morello-fvp = "override-glibc.inc"

LEAD_SONAME           = "libc.so"

INSANE_SKIP:${PN}-dev = "staticdev"
INSANE_SKIP:${PN}     = "libdir"
INSANE_SKIP:${PN}     = "dev-so"

RPROVIDES:${PN}      += "ldd rtld(GNU_HASH) musl-libc"

require ${MACHINE_INC}
require recipes-devtools/gcc/gcc-${PV}.inc
require recipes-devtools/gcc/gcc-target.inc

ARM_INSTRUCTION_SET:armv4 = "arm"
ARM_INSTRUCTION_SET:armv5 = "arm"

ARMFPARCHEXT:armv6 = "${@'+fp' if d.getVar('TARGET_FPU') == 'hard' else ''}"
ARMFPARCHEXT:armv7a = "${@'+fp' if d.getVar('TARGET_FPU') == 'hard' else ''}"
ARMFPARCHEXT:armv7ve = "${@'+fp' if d.getVar('TARGET_FPU') == 'hard' else ''}"

BBCLASSEXTEND = "nativesdk"
OVERRIDES =. "${@['', 'toolchain-${TOOLCHAIN}:']['${TOOLCHAIN}' != '']}"
OVERRIDES[vardepsexclude] += "TOOLCHAIN"

# var default init
LLVM_PATH    ??= ""
LLVM_VERSION ??= ""
LLVM_CONFIG  ??= ""
LIBCPLUSPLUS ??= ""
TC_DEPENDS   ??= ""

# /usr/lib /usr/bin etc. are used here explicitly in case this class
# would be combined with other classes that could overwrite these variables

LLVM_VERSION:toolchain-llvm-morello     = "13.0.0"
LLVM_PATH:toolchain-llvm-morello        = "${STAGING_DIR_NATIVE}/usr/bin"

INHIBIT_DEFAULT_DEPS:toolchain-llvm-morello  = "1"
INHIBIT_PACKAGE_STRIP:toolchain-llvm-morello = "1"

TC_DEPENDS:append:toolchain-llvm-morello              = " virtual/llvm-morello-native"
TC_DEPENDS:append:toolchain-llvm-morello:class-target = " virtual/llvm-morello-runtime-native virtual/musl-morello"

DEPENDS:append:toolchain-llvm-morello = "${TC_DEPENDS}"

CC:toolchain-llvm-morello          ??= "${LLVM_PATH}/clang"
CXX:toolchain-llvm-morello         ??= "${LLVM_PATH}/clang++"
CPP:toolchain-llvm-morello         ??= "${LLVM_PATH}/clang -E"
CCLD:toolchain-llvm-morello        ??= "${LLVM_PATH}/clang"
HOSTCC:toolchain-llvm-morello      ??= "${LLVM_PATH}/clang"
RANLIB:toolchain-llvm-morello      ??= "${LLVM_PATH}/llvm-ranlib"
AR:toolchain-llvm-morello          ??= "${LLVM_PATH}/llvm-ar"
AS:toolchain-llvm-morello          ??= "${LLVM_PATH}/llvm-as"
NM:toolchain-llvm-morello          ??= "${LLVM_PATH}/llvm-nm"
OBJDUMP:toolchain-llvm-morello     ??= "${LLVM_PATH}/llvm-objdump"
OBJCOPY:toolchain-llvm-morello     ??= "${LLVM_PATH}/llvm-objcopy"
STRIP:toolchain-llvm-morello       ??= "${LLVM_PATH}/llvm-strip"
STRINGS:toolchain-llvm-morello     ??= "${LLVM_PATH}/llvm-strings"
READELF:toolchain-llvm-morello     ??= "${LLVM_PATH}/llvm-readelf"
LD:toolchain-llvm-morello          ??= "${LLVM_PATH}/ld.lld"
LTO:toolchain-llvm-morello         ??= "-fuse-ld=lld"
LLVM_CONFIG:toolchain-llvm-morello ??= "${LLVM_PATH}/llvm-config"

export ${LLVM_CONFIG}

CC_PURECAP_FLAGS = "--target=${GLOBAL_ARCH_TRIPLE} --sysroot ${STAGING_DIR_TARGET}${PURECAP_SYSROOT_DIR} -march=morello -mabi=purecap"

CC_PURECAP_FLAGS += "\
           -Werror=implicit-function-declaration \
           -Werror=format \
           -Werror=undefined-internal \
           -Werror=incompatible-pointer-types \
           -Werror=cheri-capability-misuse \
           -Werror=cheri-bitwise-operations \
           "

CC_PURECAP_FLAGS += "-isystem ${STAGING_DIR_TARGET}${PURECAP_SYSROOT_DIR}/usr/src/linux-headers-morello/include"

CC:append:toolchain-llvm-morello:class-target      = " ${CC_PURECAP_FLAGS}"
CXX:append:toolchain-llvm-morello:class-target     = " ${CC_PURECAP_FLAGS}"

LD_PURECAP_FLAGS = "-L${STAGING_DIR_TARGET}${PURECAP_SYSROOT_DIR}/usr/lib -rtlib=compiler-rt -Wl,-rpath=${PURECAP_SYSROOT_DIR}/usr/lib "

LDFLAGS:append:toolchain-llvm-morello:class-target = " ${LD_PURECAP_FLAGS}"

DEPENDS:remove:toolchain-llvm-morello = "libgcc"

RDEPENDS:${PN}:append:toolchain-llvm-morello = " musl"
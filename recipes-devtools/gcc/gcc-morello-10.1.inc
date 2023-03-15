require recipes-devtools/gcc/gcc-common.inc

ARM_GCC_VERSION = "10.1"
PV              = "morello-${ARM_GCC_VERSION}"
CVE_VERSION     = "10.1"

# BINV should be incremented to a revision after a minor gcc release
BINV    = "10.1.0"

RELEASE = "morello-alp2"
PR      = "r${RELEASE}"

FILESEXTRAPATHS =. "${FILE_DIRNAME}/gcc-morello-${ARM_GCC_VERSION}:${FILE_DIRNAME}/gcc-morello-${ARM_GCC_VERSION}/backport:"

DEPENDS    =+ "mpfr gmp libmpc zlib flex-native"
NATIVEDEPS =  "mpfr-native gmp-native libmpc-native zlib-native flex-native"

LICENSE = "GPL-3.0-with-GCC-exception & GPL-3.0-only"

LIC_FILES_CHKSUM = "\
    file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
    file://COPYING3;md5=d32239bcb673463ab874e80d47fae504 \
    file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6 \
    file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
    file://COPYING.RUNTIME;md5=fe60d87048567d4fe8c8a0ed2448bcc8 \
"

BASEURI ?= "https://developer.arm.com/-/media/Files/downloads/gnu-morello/${ARM_GCC_VERSION}.${RELEASE}/srcrel/arm-gnu-toolchain-src-snapshot-${ARM_GCC_VERSION}.${RELEASE}.tar.xz"

SRC_URI = "\
           ${BASEURI} \
           file://patches/0001-gcc-4.3.1-ARCH_FLAGS_FOR_TARGET.patch \
           file://patches/0002-gcc-poison-system-directories.patch \
           file://patches/0004-64-bit-multilib-hack.patch \
           file://patches/0007-Use-the-defaults.h-in-B-instead-of-S-and-t-oe-in-B.patch \
           file://patches/0009-cpp-honor-sysroot.patch \
           file://patches/0011-Define-GLIBC_DYNAMIC_LINKER-and-UCLIBC_DYNAMIC_LINKE.patch \
           file://patches/0012-gcc-Fix-argument-list-too-long-error.patch \
           file://patches/0014-libtool.patch \
           file://patches/0015-gcc-armv4-pass-fix-v4bx-to-linker-to-support-EABI.patch \
           file://patches/0016-Use-the-multilib-config-files-from-B-instead-of-usin.patch \
           file://patches/0017-Avoid-using-libdir-from-.la-which-usually-points-to-.patch \
           file://patches/0018-export-CPP.patch \
           file://patches/0019-Ensure-target-gcc-headers-can-be-included.patch \
           file://patches/0020-Don-t-search-host-directory-during-relink-if-inst_pr.patch \
           file://patches/0023-libcc1-fix-libcc1-s-install-path-and-rpath.patch \
           file://patches/0024-handle-sysroot-support-for-nativesdk-gcc.patch \
           file://patches/0025-Search-target-sysroot-gcc-version-specific-dirs-with.patch \
           file://patches/0027-nios2-Define-MUSL_DYNAMIC_LINKER.patch \
           file://patches/0028-Add-ssp_nonshared-to-link-commandline-for-musl-targe.patch \
           file://patches/0029-Link-libgcc-using-LDFLAGS-not-just-SHLIB_LDFLAGS.patch \
           file://patches/0030-sync-gcc-stddef.h-with-musl.patch \
           file://patches/0034-libgcc_s-Use-alias-for-__cpu_indicator_init-instead-.patch \
           file://patches/0035-gentypes-genmodes-Do-not-use-__LINE__-for-maintainin.patch \
           file://patches/0037-libatomic-Do-not-enforce-march-on-aarch64.patch \
           file://patches/0041-apply-debug-prefix-maps-before-checksumming-DIEs.patch \
           file://patches/0006-If-CXXFLAGS-contains-something-unsupported-by-the-bu.patch \
           file://patches/0001-Fix-install-path-of-linux64.h.patch \
"
SRC_URI[sha256sum] = "d01995fd413d6062dbea0c6f0f3143c6e02219c58cd823f3fad8307f1627f653"

S = "${TMPDIR}/work-shared/gcc-${PV}-${PR}/arm-gnu-toolchain-src-snapshot-${ARM_GCC_VERSION}.${RELEASE}"

# Language Overrides
FORTRAN = ""
JAVA = ""

SSP ?= "--disable-libssp"
SSP:mingw32 = "--enable-libssp"

EXTRA_OECONF_BASE = "\
    ${SSP} \
    --enable-libitm \
    --enable-lto \
    --disable-bootstrap \
    --with-system-zlib \
    ${@'--with-linker-hash-style=${LINKER_HASH_STYLE}' if '${LINKER_HASH_STYLE}' else ''} \
    --enable-linker-build-id \
    --with-ppl=no \
    --with-cloog=no \
    --enable-checking=release \
    --enable-cheaders=c_global \
    --without-isl \
"

EXTRA_OECONF_INITIAL = "\
    --disable-libgomp \
    --disable-libitm \
    --disable-libquadmath \
    --with-system-zlib \
    --disable-lto \
    --disable-plugin \
    --enable-linker-build-id \
    --enable-decimal-float=no \
    --without-isl \
    --disable-libssp \
"

EXTRA_OECONF:append_aarchilp32 = " --with-abi=ilp32"

EXTRA_OECONF_PATHS = "\
    --with-gxx-include-dir=/not/exist{target_includedir}/c++/${BINV} \
    --with-sysroot=/not/exist \
    --with-build-sysroot=${STAGING_DIR_TARGET} \
"

# Is a binutils 2.26 issue, not gcc
CVE_CHECK_IGNORE += "CVE-2021-37322"

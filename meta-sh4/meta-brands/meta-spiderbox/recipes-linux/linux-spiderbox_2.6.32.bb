SUMMARY = "Linux kernel for ${MACHINE}"
LICENSE = "GPLv2"
SECTION = "kernel"
PACKAGE_ARCH = "${MACHINE_ARCH}"

KV = "2.6.32"
SRCDATE = "20200618"

COMPATIBLE_MACHINE = "hl101"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel machine_kernel_pr

DEPENDS_append_hl101 += "stlinux24-sh4-stx7109-fdma-firmware"

STM_PATCH_STR = "0217"
LINUX_VERSION = "2.6.32.71"
SRCREV = "56f768a7239d45473d02b11fc07a38bc2147c5ac"

# By default, kernel.bbclass modifies package names to allow multiple kernels
# to be installed in parallel. We revert this change and rprovide the versioned
# package names instead, to allow only one kernel to be installed.
PKG_${KERNEL_PACKAGE_NAME}-base = "kernel-base"
PKG_${KERNEL_PACKAGE_NAME}-image = "kernel-image"
RPROVIDES_${KERNEL_PACKAGE_NAME}-base = "kernel-${KERNEL_VERSION}"
RPROVIDES_kernel-image = "kernel-image-${KERNEL_VERSION}"

SRC_URI = "git://github.com/sid8796/linux-sh4-2.6.32.71.git;protocol=git;branch=stmicro \
    file://st-coprocessor.h \
    file://linux-usbwait123_stm24.patch \
    file://linux-sh4-stmmac_stm24_${STM_PATCH_STR}.patch \
    file://linux-sh4-i2c-st40-pio_stm24_${STM_PATCH_STR}.patch \
"

SRC_URI_append_hl101 = " \
    file://linux-sh4-${MACHINE}_setup_stm24_${STM_PATCH_STR}.patch \
    file://defconfig \
    "

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

export OS = "Linux"
KERNEL_OBJECT_SUFFIX = "ko"
KERNEL_IMAGETYPE = "uImage"
KERNEL_IMAGEDEST = "tmp"
KEEPUIMAGE = "yes"
PARALLEL_MAKEINST = ""

# bitbake.conf only prepends PARALLEL make in tasks called do_compile, which isn't the case for compile_modules
# So explicitly enable it for that in here
EXTRA_OEMAKE_prepend = " ${PARALLEL_MAKE} "

PACKAGES =+ "kernel-headers"
INSANE_SKIP_${PN} += "installed-vs-shipped"
FILES_kernel-headers = "${exec_prefix}/src/linux*"
FILES_${KERNEL_PACKAGE_NAME}-dev += "${includedir}/linux"
FILES_${KERNEL_PACKAGE_NAME}-image = "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} oldconfig"

do_configure_prepend() {
    oe_machinstall -m 0644 ${WORKDIR}/defconfig ${B}/.config
    sed -i "s#^\(CONFIG_EXTRA_FIRMWARE_DIR=\).*#\1\"${STAGING_DIR_HOST}/lib/firmware\"#" ${B}/.config;
}

do_shared_workdir_prepend() {
    # Workaround for missing dir required in latest oe
    mkdir -p ${B}/include/generated
    touch ${B}/include/generated/null
}

do_shared_workdir_append() {
    kerneldir=${STAGING_KERNEL_BUILDDIR}
    if [ -f include/linux/bounds.h ]; then
        mkdir -p $kerneldir/include/linux
        cp -f include/linux/bounds.h $kerneldir/include/linux/bounds.h
    fi
    if [ -f include/asm-sh/machtypes.h ]; then
        mkdir -p $kerneldir/include/asm-sh
        #ln -sf $kerneldir/include/asm-sh $kerneldir/include/asm
        cp -f include/asm-sh/machtypes.h $kerneldir/include/asm-sh
    fi
    if [ -e include/linux/utsrelease.h ]; then
        mkdir -p $kerneldir/include/linux
        cp -f include/linux/utsrelease.h $kerneldir/include/linux/utsrelease.h
    fi
}

do_install_append() {
    install -d ${D}${includedir}/linux
    install -m 644 ${WORKDIR}/st-coprocessor.h ${D}${includedir}/linux
    oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}/src/linux-${KERNEL_VERSION} ARCH=$ARCH
    mv ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
}

# hack to override kernel.bbclass...
# uimages are already built in kernel compile
do_uboot_mkimage() {
    :
}

pkg_postinst_kernel-image() {
    if [ "x$D" == "x" ]; then
        if [ -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} ] ; then
            if grep -q root=/dev/mtdblock6 /proc/cmdline; then
                flash_eraseall /dev/${MTD_KERNEL}
                nandwrite -p /dev/${MTD_KERNEL} /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
                rm -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
            else
                flash_erase /dev/${MTD_KERNEL} 0x400000 0x20
                nandwrite -s 0x400000 -p /dev/${MTD_KERNEL} /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
            fi
        fi
    fi
    true
}

do_rm_work() {
}

# extra tasks
addtask kernel_link_images after do_compile before do_install

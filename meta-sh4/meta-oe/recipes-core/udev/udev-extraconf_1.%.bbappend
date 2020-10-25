FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_sh4 = " \
       file://usbhd-automount.rules \
"

do_install_append() {
    install -m 0644 ${WORKDIR}/usbhd-automount.rules       ${D}${sysconfdir}/udev/rules.d/usbhd-automount.rules
}

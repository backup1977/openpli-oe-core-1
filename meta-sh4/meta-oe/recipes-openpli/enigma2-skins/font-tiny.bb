SUMMARY = "Enigma2 Font Tiny"
MAINTAINER = "OBH"
SECTION = "misc"
PRIORITY = "optional"
LICENSE = "CLOSED"

inherit allarch

SRC_URI = "file://tiny.ttf"

S = "${WORKDIR}"

FILES_${PN} = "/usr/*"

do_install() {
    mkdir -p ${D}/usr/share/fonts
    install -m 0644 ${S}/tiny.ttf ${D}/usr/share/fonts/
}

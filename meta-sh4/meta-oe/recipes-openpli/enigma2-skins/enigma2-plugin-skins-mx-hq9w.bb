SUMMARY = "Enigma2 Skin MX HQ9W"
MAINTAINER = "OBH"
SECTION = "misc"
PRIORITY = "optional"
LICENSE = "CLOSED"
RDEPENDS_${PN} += "font-tiny enigma2-plugin-systemplugins-bh-skin-support"

inherit gitpkgv allarch
SRCREV = "${AUTOREV}"
PV = "1.0+git${SRCPV}"
PKGV = "1.0+git${GITPKGV}"

SRC_URI= " \
	git://github.com/BlackHole/MX_HQ9W.git;protocol=git \
	file://rc.patch;patch=1 \
	file://pli.png \
	file://pli.patch;patch=1 \
"

S = "${WORKDIR}/git"

FILES_${PN} = "/usr/*"

do_compile() {
}

do_install() {
    rm -Rf ${S}/usr/uninstall
    rm -Rf ${S}/usr/share/fonts
    cp -r --preserve=mode,links ${S}/usr ${D}/
    install -m 0644 ${WORKDIR}/pli.png ${D}/usr/share/enigma2/MX_HQ9W/
}

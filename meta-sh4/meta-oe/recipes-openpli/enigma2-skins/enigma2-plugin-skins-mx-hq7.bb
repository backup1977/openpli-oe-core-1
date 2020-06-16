SUMMARY = "Enigma2 Skin MX HQ7"
MAINTAINER = "OBH"
SECTION = "misc"
PRIORITY = "optional"
LICENSE = "CLOSED"
RDEPENDS_${PN} += "font-tiny enigma2-plugin-systemplugins-bh-skin-support"

inherit gitpkgv allarch
SRCREV = "${AUTOREV}"
PV = "1.0+git${SRCPV}"
PKGV = "1.0+git${GITPKGV}"

SRC_URI=" \
	git://github.com/OBH-Skins/MX-HQ7-Pli.git;protocol=git \
	file://rc.patch;patch=1 \
	"

S = "${WORKDIR}/git"

FILES_${PN} = "/usr/*"

do_compile() {
}

do_install() {
    rm -Rf ${S}/usr/share/fonts
    cp -r --preserve=mode,links ${S}/usr ${D}/
}

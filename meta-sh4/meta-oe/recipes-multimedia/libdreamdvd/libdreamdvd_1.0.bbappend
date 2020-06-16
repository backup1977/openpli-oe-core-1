FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_sh4 += "file://libdreamdvd-1.0-support_sh4.patch"

CFLAGS_append_sh4 += " -std=gnu11"

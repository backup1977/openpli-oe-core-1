CFLAGS_append_sh4 = " -Wno-format-truncation ${@bb.utils.contains("TARGET_ARCH", "sh4", "", "-Wno-error=stringop-truncation", d)} "

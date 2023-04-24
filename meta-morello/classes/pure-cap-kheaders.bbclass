DEPENDS              += "virtual/kernel"
do_configure[depends] = "virtual/kernel:do_install"
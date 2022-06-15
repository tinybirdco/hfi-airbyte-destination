#
# Copyright (c) 2022 Airbyte, Inc., all rights reserved.
#


import sys

from destination_tinybird import DestinationTinybird

if __name__ == "__main__":
    DestinationTinybird().run(sys.argv[1:])

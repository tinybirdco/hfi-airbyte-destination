#
# Copyright (c) 2022 Airbyte, Inc., all rights reserved.
#


from typing import Any, Iterable, Mapping
import requests
import json

from airbyte_cdk import AirbyteLogger
from airbyte_cdk.destinations import Destination
from airbyte_cdk.models import AirbyteConnectionStatus, AirbyteMessage, ConfiguredAirbyteCatalog, Status, Type


class DestinationTinybird(Destination):
    def write(
        self, config: Mapping[str, Any], configured_catalog: ConfiguredAirbyteCatalog, input_messages: Iterable[AirbyteMessage]
    ) -> Iterable[AirbyteMessage]:

        datasource_name = config['datasource_name']
        api_url = config['api_url']
        api_token = config['api_token']

        events_endpoint = 'v0/events'

        params = {
            'name': datasource_name,
            'token': api_token,
        }

        url = api_url + events_endpoint

        for message in input_messages:
            if message.type == Type.RECORD:
                record = json.dumps(message.record.data)
                r = requests.post(url, params=params, data=record)
            elif message.type == Type.STATE:
                yield message

    def check(self, logger: AirbyteLogger, config: Mapping[str, Any]) -> AirbyteConnectionStatus:

        datasource_name = config['datasource_name']
        logger.debug(
            "Tinybird Destination Config Check - datasource_name: " + datasource_name)
        api_url = config['api_url']
        logger.debug("Tinybird Destination Config Check - api_url: " + api_url)
        api_token = config['api_token']
        logger.debug(
            "Tinybird Destination Config Check - api_token (ends with): " + api_token[-1])

        try:
            return AirbyteConnectionStatus(status=Status.SUCCEEDED)
        except Exception as e:
            return AirbyteConnectionStatus(status=Status.FAILED, message=f"An exception occurred: {repr(e)}")

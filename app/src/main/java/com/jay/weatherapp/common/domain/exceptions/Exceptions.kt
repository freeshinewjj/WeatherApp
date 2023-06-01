package com.jay.weatherapp.common.domain.exceptions

import java.io.IOException

class NetworkUnavailableException(message: String): IOException(message)
class ServerDataParseError(message: String): Exception(message)
class NoLocationPermissionException(message: String): Exception(message)
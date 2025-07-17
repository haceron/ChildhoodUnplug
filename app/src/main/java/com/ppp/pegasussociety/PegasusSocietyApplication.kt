package com.ppp.pegasussociety

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//--------------It initialize the Dagger Hilt framework and
// -------------also i made changes in manifest file.
@HiltAndroidApp
class PegasusSocietyApplication : Application()
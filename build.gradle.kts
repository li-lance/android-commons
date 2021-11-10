plugins {
  id("fate.android-lib-default")
  id("fate.android-compose")
}
dependencies {
  implementation(Lib.AndroidX.coreKtx)
  implementation(Lib.AndroidX.appcompat)
  implementation(Lib.timber)
  implementation(Lib.coil)
  implementation(Lib.palette)
}
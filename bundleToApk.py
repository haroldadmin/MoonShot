import subprocess
import os
from pathlib import Path

keystore = Path(os.environ['KEYSTORE'])
bundletool = os.environ['BUNDLETOOL']

bundleFile = input("Where's your app bundle? (default: ./app/release/app-release.aab): ")

bundleFilePath = Path("./app/release/app-release.aab")
if not bundleFile in ["", "\n", "\r\n"]:
  bundleFilePath = Path(bundleFile).absolute()
  print(f"Storing bundle at {bundleFilePath}")

apksPath = bundleFilePath.parent.absolute().joinpath("app-release.apks")

if os.path.isfile(apksPath):
  print("Apks file already exists, removing the existing file.")
  subprocess.call(["rm", apksPath])

print(f"Storing output at {apksPath}")
print(f"Using keystore at {keystore.absolute()}")

keystorePass = input("Keystore password: ")
keyAlias = input("Key Alias: ")
keyAliasPass = input(f"Password for key {keyAlias}: ")

print("Building Apks...")
subprocess.call(["java", "-jar", bundletool, "build-apks", f"--bundle={bundleFilePath}", f"--output={apksPath}", f"--ks={keystore}", f"--ks-pass=pass:{keystorePass}", f"--ks-key-alias={keyAlias}", f"--key-pass=pass:{keyAliasPass}"])

shouldInstallApks = input("Apks file generated. Install on device? (Y/n): ")
if shouldInstallApks in ["", "y", "Y"]:
  subprocess.call(["java", "-jar", bundletool, "install-apks", f"--apks={apksPath}"])
else:
  print("Exiting...")
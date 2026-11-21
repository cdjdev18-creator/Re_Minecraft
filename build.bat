@echo off
setlocal
set JAVA_HOME=C:\Users\seven\.jdks\ms-21.0.10
set PATH=%JAVA_HOME%\bin;C:\Users\seven\.cargo\bin;C:\msys64\mingw64\bin;%PATH%

echo Verificando Java 21...
java -version 2>&1 | findstr /i "21" >nul
if errorlevel 1 ( echo ERROR: Java 21; pause; exit /b 1 )

echo Verificando Rust...
cargo --version >nul 2>&1
if errorlevel 1 ( echo ERROR: Cargo; pause; exit /b 1 )

echo Compilando Rust (release)...
pushd rust
cargo build --release
if errorlevel 1 ( popd; echo ERROR Rust; pause; exit /b 1 )
popd

echo Limpiando build anterior...
if exist build rmdir /s /q build
if exist exe\Re_Minecraft rmdir /s /q exe\Re_Minecraft

echo Compilando Java...
call gradlew.bat clean build
if errorlevel 1 ( echo ERROR Java; pause; exit /b 1 )

echo Copiando DLL...
if not exist exe\Re_Minecraft mkdir exe\Re_Minecraft
copy /y rust\target\release\reminecraft_native.dll exe\Re_Minecraft\ >nul

echo.
echo Build completado. EXE: exe\Re_Minecraft\Re_Minecraft.exe
pause

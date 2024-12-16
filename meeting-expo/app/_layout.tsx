import { DarkTheme, DefaultTheme, ThemeProvider } from '@react-navigation/native';
import { useFonts } from 'expo-font';
import { Stack } from 'expo-router';
import * as SplashScreen from 'expo-splash-screen';
import { StatusBar } from 'expo-status-bar';
import { useEffect } from 'react';
import 'react-native-reanimated';
import { useColorScheme } from '@/hooks/useColorScheme';
import {GestureHandlerRootView} from "react-native-gesture-handler";
import Animated, {useAnimatedStyle, useSharedValue, withTiming} from "react-native-reanimated";
import SettingContext from "@/components/SettingComponentContext";


// Prevent the splash screen from auto-hiding before asset loading is complete.
SplashScreen.preventAutoHideAsync();

export default function RootLayout() {
  const colorScheme = useColorScheme();
  const [loaded] = useFonts({
    SpaceMono: require('../assets/fonts/SpaceMono-Regular.ttf'),
  });

  useEffect(() => {
    if (loaded) {
      SplashScreen.hideAsync();
    }
  }, [loaded]);

  if (!loaded) {
    return null;
  }


  const active = useSharedValue(false);
  const drawerActive = () => {
    active.value = true;
  }
  const drawerClose = () => {
    active.value = false;
  }
  const animatedStyle = useAnimatedStyle(() => {
    return {
      transform: [{scale: active.value ? withTiming(0.8) : withTiming(1)}]
    };
  })

  return (
      <GestureHandlerRootView style={{flex: 1}}>

          <Animated.View style={[{flex: 1}, animatedStyle]}>
            <SettingContext.Provider value={{drawerActive: drawerActive, drawerClose: drawerClose}}>
            <ThemeProvider value={colorScheme === 'dark' ? DarkTheme : DefaultTheme}>
              <Stack>
                <Stack.Screen name='(auths)' options={{headerShown: false}}/>
                <Stack.Screen name='(tabs)' options={{headerShown: false}}/>
                <Stack.Screen name="+not-found" options={{headerShown: false}}/>
              </Stack>
              <StatusBar style="auto" />
            </ThemeProvider>
            </SettingContext.Provider>
          </Animated.View>

      </GestureHandlerRootView>
  );
}


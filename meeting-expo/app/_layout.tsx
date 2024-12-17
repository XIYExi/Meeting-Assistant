import {DarkTheme, DefaultTheme, ThemeProvider} from '@react-navigation/native';
import {useFonts} from 'expo-font';
import {Stack} from 'expo-router';
import * as SplashScreen from 'expo-splash-screen';
import {StatusBar} from 'expo-status-bar';
import {useEffect} from 'react';
import 'react-native-reanimated';
import {useColorScheme} from '@/hooks/useColorScheme';
import {GestureHandlerRootView} from "react-native-gesture-handler";
import Animated, {
  Extrapolation,
  interpolate,
  useAnimatedStyle,
  useDerivedValue,
  useSharedValue,
  withSpring,
  withTiming
} from "react-native-reanimated";
import SettingContext from "@/components/SettingComponentContext";
import OverlayComponent from "@/components/OverlayComponent";
import DrawerComponent from "@/components/DrawerComponent";


// Prevent the splash screen from auto-hiding before asset loading is complete.
SplashScreen.preventAutoHideAsync();

export default function RootLayout() {
  const colorScheme = useColorScheme();
  const [loaded] = useFonts({
    SpaceMono: require('../assets/fonts/SpaceMono-Regular.ttf'),
  });

  const active = useSharedValue(false);
  const drawerActive = () => {
    active.value = true;
  }
  const drawerClose = () => {
    active.value = false;
  }
  const progress = useDerivedValue(() => {
    return withTiming(active.value ? 0.8 : 0);
  });
  const animatedStyle = useAnimatedStyle(() => {
    const rotateY = interpolate(
        progress.value,
        [0, 1],
        [0, -24],
        Extrapolation.CLAMP
    );
    return {
      transform: [
        {perspective: 1000},
          {scale: active.value ? withTiming(0.8) : withTiming(1)},
          {translateX: active.value ? withSpring(180) : withTiming(0)},
          {rotateY: `${rotateY}deg`, }
      ],
      borderRadius: active.value ? withTiming(20) : withTiming(0),
    };
  })

  useEffect(() => {
    if (loaded) {
      SplashScreen.hideAsync();
    }
  }, [loaded]);

  if (!loaded) {
    return null;
  }

  return (
      <GestureHandlerRootView style={{flex: 1}}>
        <SettingContext.Provider value={{drawerActive: drawerActive, drawerClose: drawerClose}}>
          <DrawerComponent />
            <Animated.View style={[{flex: 1}, animatedStyle]}>
              <ThemeProvider value={colorScheme === 'dark' ? DarkTheme : DefaultTheme}>
                <Stack>
                  <Stack.Screen name='(auths)' options={{headerShown: false}}/>
                  <Stack.Screen name='(tabs)' options={{headerShown: false}}/>
                  <Stack.Screen name="+not-found" options={{headerShown: false}}/>
                </Stack>
                <StatusBar style="auto" />
              </ThemeProvider>
              <OverlayComponent active={active} />
            </Animated.View>
        </SettingContext.Provider>
      </GestureHandlerRootView>
  );
}


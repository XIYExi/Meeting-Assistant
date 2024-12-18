import React from 'react';
import {Platform} from 'react-native';
import {HapticTab} from '@/components/HapticTab';
import TabBarBackground from '@/components/ui/TabBarBackground';
import {Colors} from '@/constants/Colors';
import {useColorScheme} from '@/hooks/useColorScheme';
import TabBarComponent from "@/components/TabBarComponent";
import {createBottomTabNavigator} from "@react-navigation/bottom-tabs";
import HomeScreen from "@/app/(tabs)/index";
import UserAppScreen from "@/app/(tabs)/user";
import TabTwoScreen from "@/app/(tabs)/explore";

const Tabs = createBottomTabNavigator();

export default function TabLayout() {
    const colorScheme = useColorScheme();

    return (
        <Tabs.Navigator
            tabBar={props => <TabBarComponent {...props} />}
            screenOptions={{
                tabBarActiveTintColor: Colors[colorScheme ?? 'light'].tint,
                headerShown: false,
                tabBarButton: HapticTab,
                tabBarBackground: TabBarBackground,
                tabBarStyle: Platform.select({
                    ios: {
                        // Use a transparent background on iOS to show the blur effect
                        position: 'absolute',
                    },
                    default: {},
                }),
            }}>
            <Tabs.Screen
                name="home"
                component={HomeScreen}
                options={{
                    title: 'Home',
                }}
            />

            <Tabs.Screen
                name="explore"
                component={TabTwoScreen}
                options={{
                    title: 'Explore',
                }}
            />

            <Tabs.Screen
                name="user"
                component={UserAppScreen}
                options={{
                    title: 'User',
                }}
            />
        </Tabs.Navigator>
    );
}

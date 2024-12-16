import {KeyboardAvoidingView} from "react-native";
import {Stack} from "expo-router";
import {Drawer} from "expo-router/drawer";

/**
 @name: SettingLayout
 @description:系统设置页面嵌套路由
 @author: xiye.Cayon
 @time: 2024-12-16 13:55:47
 */
export default function SettingLayout() {


    return (
        <KeyboardAvoidingView style={{flex: 1}}>
            <Drawer screenOptions={{
                headerStyle: {
                    backgroundColor: '#f4511e',
                },
                headerTintColor: '#fff',
                headerTitleStyle: {
                    fontWeight: 'bold',
                },
            }}>
                <Drawer.Screen name='index' options={{headerShown: false}} />
            </Drawer>
        </KeyboardAvoidingView>
    )
}


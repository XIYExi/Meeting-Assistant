import {Stack} from "expo-router";
import {KeyboardAvoidingView, SafeAreaView} from "react-native";
import {ThemedView} from "@/components/ThemedView";


export default function AuthLayout() {

    return (
        <KeyboardAvoidingView style={{ flex: 1 }} behavior='padding'>
            <ThemedView style={{flex: 1}}>
                <Stack
                    screenOptions={{
                        headerStyle: {
                            backgroundColor: '#f4511e',
                        },
                        headerTintColor: '#fff',
                        headerTitleStyle: {
                            fontWeight: 'bold',
                        },
                    }}
                    initialRouteName={'forget'}
                >
                    <Stack.Screen name='forget' options={{headerShown: false}} />
                    <Stack.Screen name='index' options={{headerShown: false}}/>
                    <Stack.Screen name='otp' options={{headerShown: false}}/>
                    <Stack.Screen name='reset' options={{headerShown: false}}/>
                </Stack>
            </ThemedView>
        </KeyboardAvoidingView>
    )
}
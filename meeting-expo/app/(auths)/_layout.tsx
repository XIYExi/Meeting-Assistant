import {KeyboardAvoidingView, SafeAreaView} from "react-native";
import {ThemedView} from "@/components/ThemedView";
import {createStackNavigator} from "@react-navigation/native/src/__stubs__/createStackNavigator";
import ForgetApp from "@/app/(auths)/forget";
import LoginApp from "@/app/(auths)/index";
import OtpAuthApp from "@/app/(auths)/otp";
import ResetPasswordApp from "@/app/(auths)/reset";

const AuthStack = createStackNavigator();

export default function AuthLayout() {
    return (
        <KeyboardAvoidingView style={{ flex: 1 }} behavior='padding'>
            <ThemedView style={{flex: 1}}>
                <AuthStack.Navigator
                    screenOptions={{
                        headerStyle: {
                            backgroundColor: '#f4511e',
                        },
                        headerTintColor: '#fff',
                        headerTitleStyle: {
                            fontWeight: 'bold',
                        },
                    }}
                    initialRouteName={'login'}
                >
                    {/* 登录页面 */}
                    <AuthStack.Screen name='login' component={LoginApp} options={{headerShown: false}}/>
                    {/* 忘记密码 - 确认邮箱页面 */}
                    <AuthStack.Screen name='forget' component={ForgetApp} options={{headerShown: false}} />
                    {/* 忘记密码 - 输入邮箱验证码页面 */}
                    <AuthStack.Screen name='otp' component={OtpAuthApp} options={{headerShown: false}}/>
                    {/* 忘记密码 - 重设新密码页面 */}
                    <AuthStack.Screen name='reset' component={ResetPasswordApp} options={{headerShown: false}}/>
                </AuthStack.Navigator>
            </ThemedView>
        </KeyboardAvoidingView>
    )
}
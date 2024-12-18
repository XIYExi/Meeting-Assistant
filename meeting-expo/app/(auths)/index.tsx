import React, {useState} from "react";
import {ActivityIndicator, Dimensions, StyleSheet, TouchableOpacity, View} from "react-native";
import {ThemedText} from "@/components/ThemedText";
import {ThemedView} from "@/components/ThemedView";
import InputComponent from '@/components/InputComponent';
import {useColorScheme} from "@/hooks/useColorScheme";
import {DrawerActions, Link, useNavigation} from "@react-navigation/native";

/**
 @name:LoginApp
 @description:登录页面
 @author: xiye.Cayon
 @time: 2024-12-15 16:45:08
 */
const _email = "1356768966@qq.com";
const _password = "288717";
const {width, height} = Dimensions.get('window');
function LoginApp() {

    const [email, setEmail] = useState<string>(_email);
    const [password, setPassword] = useState<string>(_password);
    const [loading, setLoading] = useState<boolean>(false);
    const [errors, setErrors] = useState<any>([]);
    const hasErrors = (key: any) => (errors.includes(key) ? styles.hasErrors : null);
    const colorScheme = useColorScheme();

    const navigation = useNavigation();
    const handle = () => {
        navigation
    }

    return (
        <React.Fragment>
            <ThemedView style={styles.loginContainer}>
                {/* 上半部文字介绍 */}
                <ThemedView style={styles.ImageWrapper}>
                    <ThemedView style={styles.TextInnerWrapper}>
                        <ThemedText style={styles.title}>Meeting Assistant</ThemedText>
                        <TouchableOpacity onPress={handle}>
                            <ThemedText style={styles.subTitle}>
                                西湖论剑大会AI+会议助手智能体应用.
                            </ThemedText>
                        </TouchableOpacity>

                        <ThemedText style={styles.descTitle}>
                            请输入您的电子邮箱和密码以登录
                        </ThemedText>
                    </ThemedView>
                </ThemedView>

                {/* 下半部登录和忘记密码 */}
                <ThemedView style={styles.InputWrapper}>
                    <View style={styles.innerLoginWrapper}>
                        <InputComponent
                            label='账号'
                            error={hasErrors('username')}
                            style={[styles.input, hasErrors('username')]}
                            defaultValue={email}
                            onChangeText={(text:string) => setEmail(text)}
                        />

                        <InputComponent
                            label='密码'
                            error={hasErrors('password')}
                            style={[styles.input, hasErrors('password')]}
                            defaultValue={password}
                            textContentType={'password'}
                            secureTextEntry={true}
                            onChangeText={(text:string) => setPassword(text)}
                        />

                        <View style={styles.AddtionTextWrapper}>
                            <Link screen='forget'>
                                <ThemedText style={styles.AdditionText}>忘记密码</ThemedText>
                            </Link>
                        </View>

                        <View style={styles.ButtonWrapper}>
                            <Link screen='tabs'>
                                <ThemedView style={[styles.LoginButton, {
                                    borderColor: colorScheme === 'dark' ? 'white' : 'black'
                                }]}>
                                    {loading ? (
                                        <ActivityIndicator size="small" color="white" />
                                    ) : (
                                        <ThemedText style={{
                                            fontWeight: '600',
                                            textAlign: 'center',
                                        }}>
                                            登录
                                        </ThemedText>
                                    )}
                                </ThemedView>
                            </Link>

                            <Link screen=''>
                                <View style={styles.RegisterButton}>
                                    <ThemedText style={{
                                        textDecorationLine: "underline",
                                        textAlign: 'center',
                                        fontWeight: '600',
                                    }}>
                                        注册
                                    </ThemedText>
                                </View>
                            </Link>
                        </View>
                    </View>
                </ThemedView>
            </ThemedView>
        </React.Fragment>
    )
}


const styles = StyleSheet.create({
    AddtionTextWrapper: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
    },
    AdditionText: {
        fontSize: 15,
        opacity: 0.8
    },
    loginContainer: {
        flex: 1,
    },
    ImageWrapper: {
      width: width,
      height: height * .5,
    },
    InputWrapper: {
        width: width,
        height: height * .5,
    },
    TextInnerWrapper: {
        paddingHorizontal: 20,
        position: 'absolute',
        bottom: height * .5 * .2 + 30,
    },
    innerLoginWrapper: {
        shadowColor: 'white',
        shadowOffset: {
            width: 0,
            height: 4,
        },
        shadowOpacity: .3,
        shadowRadius: 14,
        marginHorizontal: 20,
        paddingHorizontal: 10,
        paddingVertical: 20,
        borderRadius: 15,
        top: -height * .5 * .2,
        height: height* 0.5,
    },
    title: {
        marginTop: 24,
        fontSize: 48,
        fontStyle: 'italic',
        fontWeight: '900',
        lineHeight: 65,
        flexWrap: 'wrap',
        width: width * .65,
    },
    subTitle: {
        marginTop: 5,
        fontSize: 18,
        fontStyle: 'normal',
        fontWeight: '500',
        lineHeight: 28.8,
        letterSpacing: 0.2,
    },
    descTitle: {
        marginTop: 12,
        fontWeight: '300',
    },
    input: {
        borderRadius: 0,
        borderWidth: 0,
        borderBottomColor: "#C5CCD6",
        borderBottomWidth: StyleSheet.hairlineWidth
    },
    hasErrors: {
        borderBottomColor: "#F3534A",
    },
    LoginButton: {
        borderWidth: 1,
        width: width,
        paddingVertical: 10,
        borderRadius: 16,
    },
    RegisterButton: {
        paddingVertical: 20,
        width: width,
    },
    ButtonWrapper: {
        marginTop: 30,
        height: height * .3,

    },
});


export default LoginApp;
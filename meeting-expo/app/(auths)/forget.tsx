import React, {useState} from "react";
import {useNavigation} from "expo-router";
import HeaderComponent from "@/components/HeaderComponent";
import {Pressable, SafeAreaView, StyleSheet} from "react-native";
import {ThemedView} from "@/components/ThemedView";
import {ThemedText} from "@/components/ThemedText";
import PageComponent from "@/components/PageComponent";
import InputComponent from "@/components/InputComponent";
import ButtonComponent from "@/components/ButtonComponent";

/**
 @name:忘记密码页面1 - 验证邮箱
 @description: 发送邮箱验证码，会验证用户邮箱是否注册
 @author: xiye.Cayon
 @time: 2024-12-15 19:26:42
 */
interface IForageAuthEmailAppProps {

}

function ForgetApp(props: IForageAuthEmailAppProps) {
    const navigation = useNavigation();
    const [email, setEmail] = useState<string>('');


    const handlePressToVerifyEmail = () => {

    }

    return (
        <React.Fragment>
            <SafeAreaView style={{flex: 1}}>
                <ThemedView style={{flex: 1}}>
                    <HeaderComponent
                        backHref={'/(auths)'}
                        page='忘记密码'
                    />

                    <PageComponent>
                        <ThemedText style={styles.title}>重置您的密码🔑</ThemedText>
                        <ThemedText style={styles.subTitle}>
                            请输入您的电子邮件，我们将在下一步发送OTP代码以重置您的密码。
                        </ThemedText>


                        <ThemedView style={{marginTop: 20}}>
                            <InputComponent
                                label='邮箱'
                                error={false}
                                style={[styles.input]}
                                onChangeText={(text:string) => setEmail(text)}
                            />

                            <Pressable onPress={handlePressToVerifyEmail}>
                                <ButtonComponent text='提交' style={{
                                    marginTop: 15
                                }}/>
                            </Pressable>
                        </ThemedView>

                    </PageComponent>
                </ThemedView>
            </SafeAreaView>
        </React.Fragment>
    )
}


const styles = StyleSheet.create({
    title: {
        marginTop: 10,
        fontSize: 28,
        fontStyle: 'normal',
        fontWeight: '600',
        lineHeight: 36,
    },
    subTitle: {
        marginTop: 16,
        fontSize: 16,
        fontStyle: 'normal',
        fontWeight: '400',
        lineHeight: 28.8,
        letterSpacing: 0.2,
    },
    input: {

    },
});

export default ForgetApp;
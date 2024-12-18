import React, {useState} from "react";
import {Pressable, SafeAreaView, StyleSheet} from "react-native";
import {ThemedView} from "@/components/ThemedView";
import HeaderComponent from "@/components/HeaderComponent";
import PageComponent from "@/components/PageComponent";
import {ThemedText} from "@/components/ThemedText";
import ButtonComponent from "@/components/ButtonComponent";
import CodeInputComponent from "@/components/CodeInputComponent";
import {useNavigation} from "@react-navigation/native";


interface IOtpAuthAppProps {

}
function OtpAuthApp(props: IOtpAuthAppProps) {

    const [value, setValue] = useState<string>('');
    const navigation = useNavigation();
    const handleCheck = () => {
        // todo 校验otp code是否正确

        // todo 验证邮箱是否正确

        // @ts-ignore
        navigation.navigate('reset')
    }

    return(
        <React.Fragment>
            <SafeAreaView style={{flex:1}}>
                <ThemedView style={{flex: 1}}>
                    <HeaderComponent
                        useRedirect
                        backHref={'login'}
                        page='忘记密码'
                    />

                    <PageComponent>

                        <ThemedText style={styles.title}>OTP代码验证🔐</ThemedText>
                        <ThemedText style={styles.subTitle}>我们已向您的电子邮件发送OTP代码  .在下面输入OTP代码进行验证。</ThemedText>

                        <ThemedView style={{marginTop: 25}}>

                            <Pressable onPress={handleCheck}>
                                <CodeInputComponent style={styles.codeInput} value={value} onValueChange={setValue} />

                                <ButtonComponent text={'继续'} style={{marginTop: 48}}/>
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
        lineHeight: 50,
    },
    subTitle: {
        marginTop: 16,
        fontSize: 16,
        fontStyle: 'normal',
        fontWeight: '400',
        lineHeight: 28.8,
        letterSpacing: 0.2,
    },
    codeInput: {
        marginTop: 16,
    },
});

export default OtpAuthApp;
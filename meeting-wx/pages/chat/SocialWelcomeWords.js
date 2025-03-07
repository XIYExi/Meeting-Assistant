/**
 * SocialApp头部欢迎词，
 * 根据时间显示不同的欢迎词
 */


export function SocialWelcomeWords() {}


const morning = [
    '寒雪梅中尽，春风柳上归。',
    '小雨晨光内，初来叶上闻。',
    '山静似太古，日长如小年。',
    '秦楼出佳丽，正值朝日光。',
    '一日之际在于晨。'
]

const moon = [
    '树里鸣蝉咽，宫中午漏长。',
    '黄鸟坐中午，青山意外春。',
    '日中午，一部笙歌谁解舞。',
    '白云半枕山中午，犹梦乘槎去问津。',
    '天际阴云翳，山中午饷过。'
]

const afternoon = [
    '胸中邱壑经营巧，留下午桥别墅。',
    '松棚叟卖浆，棚下午风凉。',
    '下午繁阴合，柴门静砌苔。',
    '阶前春草遍，林下午风和。',
    '清晨灾幸免，下午避难周。'
]

const night = [
    '夜深知雪重，时闻折竹声。',
    '明夜扁舟去，和月载离愁。',
    '山中一夜雨，树杪百重泉。',
    '柴门闻犬吠，风雪夜归人。',
    '愿我如星君如月，夜夜流光相皎洁。'
]

const defaults = [
    '不妨出去走走，不要辜负大好时光',
    '很高兴见到你',
    '今日还有任务没有完成~',
    '距离开学的日子越来越近',
    '又是美好的一天'
]

SocialWelcomeWords.prototype.randomWords = function (type) {
    // [n, m] [0, 4]
    //Math.floor(Math.random() * (m - n + 1)) + n;
    const randomIdx = Math.floor(Math.random() * 5);

    switch (type) {
        case 1:
            return morning[randomIdx];
        case 2:
            return moon[randomIdx];
        case 3:
            return afternoon[randomIdx];
        case 4:
            return night[randomIdx];
        default:
            return defaults[randomIdx];
    }
}

SocialWelcomeWords.prototype.getTimeWords = function () {
    const hour = new Date().getHours();
    if (hour < 11){
        return '早上好';
    }
    else if (hour >= 11 && hour < 14){
        return '中午好';
    }
    else if (hour >= 14 && hour < 19){
        return '下午好';
    }
    else{
        return '晚上好';
    }
}
export function getDateFormat(date) {
    const day = ("0" + date.getDate()).slice(-2)
    const month = date.getMonth() + 1;
    const year = date.getFullYear();

    return year + '-' + month + '-' + day;
} 
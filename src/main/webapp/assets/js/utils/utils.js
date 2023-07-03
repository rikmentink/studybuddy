export function formDataToJson(form) {
    let formData = new FormData(form);
    let jsonData = {};

    formData.forEach((value, key) => {
        jsonData[key] = value
    });

    return jsonData;
}
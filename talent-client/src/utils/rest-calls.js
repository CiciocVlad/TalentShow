import { TALENT_BASE_URL } from './consts'

function status(response) {
    if (response.status >= 200 && response.status < 300)
        return Promise.resolve(response)
    return Promise.reject(new Error(response.statusText))
}

const json = response => response.json()

export function getConcurenti() {
    let headers = new Headers()
    headers.append('Accept', 'application/json')
    let myInit = {
        method: 'GET',
        headers: headers,
        mode: 'cors'
    }
    let request = new Request(TALENT_BASE_URL + '/concurenti', myInit)

    return fetch(request)
        .then(status)
        .then(json)
        .then(data => data)
        .catch(error => error)
}
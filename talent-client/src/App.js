import React, { useState, useEffect } from 'react'
import './App.css'
import { getConcurenti } from './utils/rest-calls'
import ConcurentTable from './ConcurentiTable'

export default function App() {
    const concurentDefault = [{
        id: 0,
        name: 'name',
        result: 'no result'
    }]

    const [concurenti, setConcurenti] = useState(concurentDefault)

    useEffect(() => {
        getConcurenti().then(c => setConcurenti(c))
    })

    return (
        <div className="App">
            <h1 className="title">Talent Managment</h1>
            <ConcurentTable concurenti={concurenti} />
        </div>
    )
}
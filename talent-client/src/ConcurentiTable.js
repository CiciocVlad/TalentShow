import React from 'react'
import './App.css'

function ConcurentRow(props) {
    return (
        <tr>
            <td>{props.concurent.name}</td>
            <td>{props.concurent.result}</td>
        </tr>
    )
}

export default function ConcurentTable(props) {
    let rows = []
    props.concurenti.forEach(concurent => {
        rows.push(<ConcurentRow concurent={concurent} key={concurent.id} />)
    })
    return (
        <div className="concurentTable">
            <table className="center">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Result</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>
        </div>
    )
}
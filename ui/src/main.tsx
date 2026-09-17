import React, { useEffect, useState } from 'react'
import { createRoot } from 'react-dom/client'
import './styles.css'

type Employee = {
  id: number
  name: string
  email: string
  title: string
  departmentName: string
}

type Page<T> = {
  content: T[]
  number: number
  totalPages: number
  totalElements: number
  first: boolean
  last: boolean
}

const API = 'http://localhost:8080/api/employees/search'

function App() {
  const [keyword, setKeyword] = useState('')
  const [department, setDepartment] = useState('')
  const [page, setPage] = useState(0)
  const [data, setData] = useState<Page<Employee> | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const load = async () => {
    setLoading(true)
    setError('')
    const params = new URLSearchParams({ page: String(page), size: '5', sortBy: 'name', direction: 'asc' })
    if (keyword.trim()) params.set('keyword', keyword.trim())
    if (department.trim()) params.set('department', department.trim())
    try {
      const response = await fetch(`${API}?${params}`)
      if (!response.ok) throw new Error(`HTTP ${response.status}`)
      setData(await response.json())
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to load data')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [page])

  const search = (event: React.FormEvent) => {
    event.preventDefault()
    if (page === 0) load()
    else setPage(0)
  }

  return (
    <main className="container">
      <h1>Employee Search</h1>
      <p className="subtitle">Spring Boot pagination + React/TypeScript display</p>

      <form className="filters" onSubmit={search}>
        <input value={keyword} onChange={e => setKeyword(e.target.value)} placeholder="Name, email or title" />
        <input value={department} onChange={e => setDepartment(e.target.value)} placeholder="Department" />
        <button type="submit">Search</button>
      </form>

      {error && <div className="error">{error}</div>}
      {loading ? <p>Loading…</p> : data && (
        <>
          <div className="meta">{data.totalElements} result(s)</div>
          <table>
            <thead><tr><th>Name</th><th>Email</th><th>Title</th><th>Department</th></tr></thead>
            <tbody>
              {data.content.map(employee => (
                <tr key={employee.id}>
                  <td>{employee.name}</td><td>{employee.email}</td><td>{employee.title}</td><td>{employee.departmentName}</td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="pagination">
            <button disabled={data.first} onClick={() => setPage(p => p - 1)}>Previous</button>
            <span>Page {data.totalPages === 0 ? 0 : data.number + 1} of {data.totalPages}</span>
            <button disabled={data.last || data.totalPages === 0} onClick={() => setPage(p => p + 1)}>Next</button>
          </div>
        </>
      )}
    </main>
  )
}

createRoot(document.getElementById('root')!).render(<React.StrictMode><App /></React.StrictMode>)

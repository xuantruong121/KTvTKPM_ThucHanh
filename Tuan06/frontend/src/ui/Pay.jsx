import { useEffect, useState, useRef } from 'react'
import { useLocation } from 'react-router-dom'
import toast from 'react-hot-toast'
import { CheckCircle, AlertCircle, Terminal, Send } from 'lucide-react'
import { api } from '../api/client.js'

export default function Pay() {
  const loc = useLocation()
  const [orderId, setOrderId] = useState('')
  const [method, setMethod] = useState('COD')
  const [loading, setLoading] = useState(false)
  const [logs, setLogs] = useState([])
  const logEndRef = useRef(null)

  const addLog = (msg, prefix = 'INFO') => {
    const time = new Date().toLocaleTimeString()
    setLogs(prev => [...prev, { time, msg, prefix }])
  }

  useEffect(() => {
    const params = new URLSearchParams(loc.search)
    const id = params.get('orderId')
    if (id) {
      setOrderId(String(id))
      addLog(`He thong phat hien Order ID #${id} tu trang gio hang.`, 'CLIENT')
    }
  }, [loc.search])

  useEffect(() => {
    logEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [logs])

  async function submit(e) {
    e.preventDefault()
    setLoading(true)
    addLog(`Dang gui yeu cau thanh toan cho don hang #${orderId}...`, 'NETWORK')
    
    const tid = toast.loading('Đang xử lý thanh toán...')
    
    try {
      const res = await api.post('/api/payments', { orderId: Number(orderId), method })
      toast.success('Thanh toán thành công!', { id: tid })
      addLog(`Thanh toan thanh cong! Payment ID: ${res.data.id}`, 'SUCCESS')
      addLog(`Order #${orderId} da duoc cap nhat trang thai PAID.`, 'EVENT')
    } catch (e2) {
      const msg = e2?.response?.data?.message || 'Thanh toán thất bại'
      toast.error(msg, { id: tid })
      addLog(`Loi: ${msg}`, 'ERROR')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="pay-container">
      <div className="card">
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '1.5rem' }}>
          <CheckCircle className="accent-icon" />
          <h2 style={{ margin: 0 }}>Thanh toán đơn hàng</h2>
        </div>
        
        <form className="form" onSubmit={submit}>
          <div className="form-group">
            <label>Mã đơn hàng (Order ID)</label>
            <input 
              value={orderId} 
              onChange={(e) => setOrderId(e.target.value)} 
              placeholder="Ví dụ: 1" 
              required
            />
          </div>

          <div className="form-group">
            <label>Phương thức thanh toán</label>
            <select value={method} onChange={(e) => setMethod(e.target.value)}>
              <option value="COD">Thanh toán khi nhận hàng (COD)</option>
              <option value="BANKING">Chuyển khoản Ngân hàng</option>
            </select>
          </div>

          <button className="btn primary" disabled={loading || !orderId} style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px' }}>
            {loading ? 'Đang xử lý...' : <><Send size={18} /> Xác nhận thanh toán</>}
          </button>
        </form>
        
        <p className="hint">
          <AlertCircle size={14} style={{ verticalAlign: 'middle', marginRight: '4px' }} />
          Sau khi nhấn, hãy kiểm tra Log bên dưới và Console của Payment Service.
        </p>
      </div>

      <div className="card" style={{ padding: '1.5rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '1rem' }}>
          <Terminal size={20} color="#f59e0b" />
          <h3 style={{ margin: 0, fontSize: '1rem' }}>Demo Logs (Live Visualization)</h3>
        </div>
        
        <div className="demo-logger">
          {logs.length === 0 && <div className="log-entry" style={{ color: '#666' }}>Chưa có hoạt động nào...</div>}
          {logs.map((log, i) => (
            <div key={i} className="log-entry">
              <span className="log-time">[{log.time}]</span>
              <span className="log-prefix">[{log.prefix}]</span>
              <span>{log.msg}</span>
            </div>
          ))}
          <div ref={logEndRef} />
        </div>
        <p style={{ fontSize: '0.75rem', color: '#94a3b8', marginTop: '0.5rem', fontStyle: 'italic' }}>
          * Log này mô phỏng các bước giao tiếp giữa Frontend và Payment Service để minh chứng lỗi/thành công.
        </p>
      </div>
    </div>
  )
}


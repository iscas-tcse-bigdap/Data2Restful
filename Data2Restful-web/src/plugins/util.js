/**
 * util.js
 * 用户封装公共的方法
 * 
 */

 /**
  * localStorage 存储k-v
  * @param {*} key 
  * @param {*} value 
  */
export const localSave = (key, value) => {
  localStorage.setItem(key, value)
}

/**
 * 通过key 获取localStorage值
 * @param key 
 */
export const localRead = (key) => {
  return localStorage.getItem(key) || ''
}

export const localRemove = (key) => {
  return localStorage.removeItem(key)
}

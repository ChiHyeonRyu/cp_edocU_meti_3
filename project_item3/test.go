func main() {
	var i, j, k int
	var rem, sum int
	
	i = 2
	for i <= 500 {
		sum = 0
		k = i / 2
		j = 1
		for j <= k {
			rem = i % j
			if (rem == 0) {
				sum += j
			}
			j++
		}
		if (i == sum) {
			write(i)
		}
		i++
	}
}
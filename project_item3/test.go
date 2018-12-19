func main() {
	var i int
	var j int
	var k int
	var rem int
	var sum int
	
	i = 2
	for i <= 500 {
		sum = 0
		k = i / 2
		j = 1
		for j <= k {
			rem = i % j
			if (rem == 0) {
				sum = sum + j
			}
			++j
		}
		if (i == sum) {
			write(i)
		}
		++i
	}
}
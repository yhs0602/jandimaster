import java.io.File

fun executeCurlCommand(curlCommand: String, outputPath: String) {
    try {
        // curl 명령어 수정
        // replace count=50& -> count=500000000&
        val modifiedCommand =
            curlCommand.trim().replace("count=50&", "count=500000000&")
                .replace("\n", " ")
                .replace("\\", " ")
                .split("\\s+".toRegex())
                .toList()
                .map {
                    it.trim()
                }
        println(modifiedCommand)
        // 명령어 실행
        val builder = ProcessBuilder(modifiedCommand)
        builder.redirectOutput(File(outputPath))
        builder.redirectError(File("out.txt"))
        val p = builder.start() // may throw IOException
        p.waitFor()

//        val result = process.inputStream.bufferedReader().use { it.readText() }
//
//        // 결과를 파일에 쓰기
//        File(outputPath).writeText(result)
    } catch (e: Exception) {
        // 예외 처리 로직
        e.printStackTrace()
    }
}
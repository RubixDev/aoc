import data from './leaderboard.json' with { type: 'json' }

const daySeconds = 24 * 60 * 60
const DAYS = 25
const NAME_PADDING = 18

function formatDuration(seconds: number): string {
    return new Date(1000 * seconds).toISOString().substring(11, 19).replace(/^[0:]+/, "")
}

console.log(`${' '.repeat(NAME_PADDING)} ${Array.from({ length: DAYS }, (_, i) => `Day ${i + 1}`.padStart(8, ' ')).join(' ')}`)
for (const member of Object.values(data.members).sort((a, b) => b.local_score - a.local_score)) {
    let part1Times = Array.from({ length: DAYS }, (_, i) => member.completion_day_level[i + 1 + '']).map(e => e !== undefined && e['1'] !== undefined ? e['1'].get_star_ts : null)
    let part2Times = Array.from({ length: DAYS }, (_, i) => member.completion_day_level[i + 1 + '']).map(e => e !== undefined && e['2'] !== undefined ? e['2'].get_star_ts : null)
    let part1Diffs = part1Times.map((ts, i) => ts === null ? null : (ts - (data.day1_ts + i * daySeconds)))
    let part2Diffs = part2Times.map((ts, i) => ts === null ? null : (ts - (data.day1_ts + i * daySeconds)))
    let part1Strings = part1Diffs.map(diff => diff === null ? '' : diff > daySeconds ? '>24h' : formatDuration(diff)).map(s => s.padStart(8, ' '))
    let part2Strings = part2Diffs.map(diff => diff === null ? '' : diff > daySeconds ? '>24h' : formatDuration(diff)).map(s => s.padStart(8, ' '))

    console.log(`${(member.name || `anonymous #${member.id}`).padStart(NAME_PADDING, ' ')} ${part1Strings.join(' ')}`)
    console.log(`${' '.repeat(NAME_PADDING)} ${part2Strings.join(' ')}`)
    console.log()
}

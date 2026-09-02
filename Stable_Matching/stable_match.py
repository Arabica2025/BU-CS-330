# stable matching problem
## 1. need to define the preferences of men and women
### men | women
###  A  |   X
###  B  |   Y
###     |   Z


men_pref = {    'Mike':   ['Rachel', 'Katrina', 'Donna', 'Sheila'],
    'Harvey': ['Donna', 'Katrina', 'Rachel', 'Sheila'],
    'Louis':  ['Sheila', 'Donna', 'Katrina', 'Rachel'],
    'Logan':  ['Rachel', 'Katrina', 'Donna', 'Sheila']}
women_pref = {    'Rachel':  ['Mike', 'Logan', 'Harvey', 'Louis'],
    'Donna':   ['Harvey', 'Louis', 'Mike', 'Logan'],
    'Katrina': ['Mike', 'Harvey', 'Louis', 'Logan'],
    'Sheila':  ['Louis', 'Logan', 'Harvey', 'Mike']}
## lists of men and women who are free
men_free = list(men_pref.keys())
women_free = list(women_pref.keys())

## create a dictionary to store the matches
men_match = {man : '' for man in men_pref.keys()}
key_list = list(men_pref.keys())

while len(men_free) > 0: # loop until there are no men free
    for man in key_list: # loop through the men list
        for woman in men_pref[man]: # loop through each man's preference list
            if woman not in list(men_match.values()): # if the woman is not already matched; case 1: both man and woman are unmatched
                men_match[man] = woman # match the woman with the corresponding man
                men_free.remove(man) # remove the matched man from the free men list
                print(f"{man} is matched with {woman}. {man} is no longer free. We have {len(men_free)} men left to match")
                break # get out of the loop since we have matched the couple
            elif woman in list(men_match.values()): # if the woman is already matched; case 2: woman is matched with another man
                current_man = list(men_match.keys())[list(men_match.values()).index(woman)] # get the current man who is matched with the woman
                w_list = women_pref.get(woman) # get the woman's preference list ## get(key) <- bring the value of the dictionary key
                if w_list.index(man) < w_list.index(current_man): # if the currently matched man is less preferred than the new man; case 2a: woman prefers the new
                    men_match[man] = woman
                    men_free.remove(man) # remove the matched
                    men_match[current_man] = '' # set the current man to be unmatched
                    men_free.append(current_man) # add the current man back to the free
                    print(f"{woman} used to be matched with {current_man} but now is matched with {man}. {current_man} is now free.")
                    
                


